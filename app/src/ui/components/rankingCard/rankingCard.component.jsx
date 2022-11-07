import "./rankingCard.style.css";
import { STAR_ICON } from "../../../assets";

export function RankingCard({ name, ranking }) {
  return (
    <div className="ranking-card">
      <div className="ranking-card__icon">
        <img src={STAR_ICON} alt="icone de contorno de uma estrela" />
        <p>{ranking}</p>
      </div>
      <p className="ranking-card__name">{name}</p>
    </div>
  );
}
