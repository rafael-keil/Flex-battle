import "./training.style.css";
import {
  Game,
  TransformTip,
  Loading,
  TextAreaGame,
  Header,
} from "../../components";
import { useEffect, useState } from "react";
import {
  kebabCaseStringIntoObjectCamelCase,
  validadeResponse,
} from "../../../utils";
import { useFbApi } from "../../../hooks";
import { DefaultButton } from "../../components/";
import { DONUT_BIG, TRAINING_ICON } from "../../../assets";
import { PATH } from "../../../constants";

export function Training() {
  const [input, setInput] = useState("");
  const [challenge, setChallenge] = useState(null);
  const fbApi = useFbApi();
  const [isLoading, setIsLoading] = useState(true);
  const [focus, setFocus] = useState(true);

  useEffect(() => {
    getRandomChallenge();
  }, []);

  async function getRandomChallenge() {
    try {
      const response = await fbApi.getRandomChallenge();
      setChallenge(response);

      setInput("");
      setIsLoading(false);
      setFocus(!focus);
    } catch {}
  }

  return (
    <div className="training__main">
      <div className="training__backgroun">
        <Header
          img={TRAINING_ICON}
          menuButton={true}
          currentUrl={PATH.TRAINING}
        />

        <div className="training__infos">
          <div className="training__hint">
            {challenge ? <TransformTip text={challenge.hint} /> : null}
          </div>

          <div className="training__text-button">
            <img
              src={DONUT_BIG}
              className="training__donnut-back"
              alt="donut rosa de fundo"
            />

            <TextAreaGame onChange={setInput} focus={focus} value={input} />

            <div className="training__button">
              {challenge &&
              !validadeResponse(
                challenge.answer,
                kebabCaseStringIntoObjectCamelCase(input)
              ) ? (
                <DefaultButton onClick={() => getRandomChallenge()}>
                  Proximo
                </DefaultButton>
              ) : null}
            </div>
          </div>
        </div>
      </div>

      <div className="training__game">
        {challenge ? (
          <Game
            input={kebabCaseStringIntoObjectCamelCase(input)}
            objects={challenge.objects}
            answer={challenge.answer}
            size={"big"}
          />
        ) : null}
      </div>

      <Loading isLoading={isLoading} />
    </div>
  );
}
