import "./ranking.style.css";
import { MenuContainer, Loading } from "../../components";
import { Header, DefaultRanking } from "../../components";
import { TROPHY } from "../../../assets";
import { PATH } from "../../../constants";
import { useFbApi } from "../../../hooks";
import { useEffect, useState } from "react";

export function Ranking() {
  const [ranking, setRanking] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const fbApi = useFbApi();

  useEffect(() => {
    async function getResponse() {
      try {
        const response = await fbApi.geRanking();

        setRanking(response);
      } catch {}

      setIsLoading(false);
    }

    getResponse();
  }, [fbApi]);

  return (
    <div className="ranking">
      <Header img={TROPHY} menuButton={true} currentUrl={PATH.RANKING} />

      <MenuContainer>
        <DefaultRanking ranking={ranking} />
      </MenuContainer>

      <Loading isLoading={isLoading} />
    </div>
  );
}
