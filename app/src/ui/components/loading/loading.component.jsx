import { DONUT_BIG } from "../../../assets";
import "./loading.style.css";

export function Loading({ isLoading }) {
  if (isLoading) {
    return (
      <div className="loading__container">
        <img
          src={DONUT_BIG}
          className="loading__donut"
          alt="imagem de donut girando"
        />
      </div>
    );
  }

  return null;
}
