import ReviewCard from '../common/ReviewCard/ReviewCard';
import SectionHeader from '../common/SectionHeader/SectionHeader';
import * as S from './ReviewListSection.style';

type Props = {
  data: {
    id: number;
    profileImage: string;
    username: string;
    rating: number;
    content: string;
  }[];
};

function ReviewListSection({ data }: Props) {
  const reviewCardList = data.map(
    ({ id, profileImage, username, rating, content }) => (
      <ReviewCard
        key={id}
        profileImage={profileImage}
        username={username}
        rating={rating}
        content={content}
      />
    )
  );
  return (
    <S.Container>
      <SectionHeader>
        <S.Title>최근 후기</S.Title>
      </SectionHeader>
      <S.Wrapper>{reviewCardList}</S.Wrapper>
    </S.Container>
  );
}

export default ReviewListSection;