package com.woowacourse.f12.domain.member;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.woowacourse.f12.domain.member.QMember.member;

public class MemberRepositoryCustomImpl extends QuerydslRepositorySupport implements MemberRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public MemberRepositoryCustomImpl(final JPAQueryFactory jpaQueryFactory) {
        super(Member.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Slice<Member> findBySearchConditions(final String keyword, final CareerLevel careerLevel,
                                                final JobType jobType,
                                                final Pageable pageable) {
        final JPAQuery<Member> jpaQuery = jpaQueryFactory.select(member)
                .from(member)
                .where(
                        containsKeyword(keyword),
                        eqCareerLevel(careerLevel),
                        eqJobType(jobType)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(makeOrderSpecifiers(pageable));

        return toSlice(pageable, jpaQuery.fetch());
    }

    private OrderSpecifier[] makeOrderSpecifiers(final Pageable pageable) {
        return pageable.getSort()
                .stream()
                .map(this::toOrderSpecifier)
                .collect(Collectors.toList()).toArray(OrderSpecifier[]::new);
    }

    private OrderSpecifier toOrderSpecifier(final Sort.Order sortOrder) {
        final Order orderMethod = toOrder(sortOrder);
        final PathBuilder<Member> pathBuilder = new PathBuilder<>(member.getType(), member.getMetadata());
        return new OrderSpecifier(orderMethod, pathBuilder.get(sortOrder.getProperty()));
    }

    private Order toOrder(final Sort.Order sortOrder) {
        if (sortOrder.isAscending()) {
            return Order.ASC;
        }
        return Order.DESC;
    }

    private BooleanExpression containsKeyword(final String keyword) {
        if (Objects.isNull(keyword) || keyword.isBlank()) {
            return null;
        }
        return member.gitHubId.contains(keyword);
    }

    private BooleanExpression eqCareerLevel(final CareerLevel careerLevel) {
        if (Objects.isNull(careerLevel)) {
            return null;
        }
        return member.careerLevel.eq(careerLevel);
    }

    private BooleanExpression eqJobType(final JobType jobType) {
        if (Objects.isNull(jobType)) {
            return null;
        }
        return member.jobType.eq(jobType);
    }

    private Slice<Member> toSlice(final Pageable pageable, final List<Member> members) {
        if (members.size() > pageable.getPageSize()) {
            members.remove(members.size() - 1);
            return new SliceImpl<>(members, pageable, true);
        }
        return new SliceImpl<>(members, pageable, false);
    }
}