import "./previewRanking.style.css";
import { TROPHY } from "../../../assets";

export function PreviewRanking({ ranking }) {
  return (
    <div>
      <div className="preview-ranking__container">
        <div className="preview-ranking__title">
          <p className="preview-ranking__title-name">Os Melhores </p>
          <img
            src={TROPHY}
            className="preview-ranking__title-img"
            alt="icone de troféu"
          />
        </div>

        {ranking.map((user) => (
          <div className="preview-ranking__list-item">
            <p className="preview-ranking__list-item-name">{user.name}</p>
            <div className="preview-ranking__list-item-points">
              <p>{user.ranking}</p>
              <p className="preview-ranking__list-item-points-detail">pt</p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
