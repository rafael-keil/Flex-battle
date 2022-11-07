import "./defaultMenu.style.css";
import { PATH } from "../../../constants";
import { useHistory } from "react-router";
import {
  OptionsButton,
  MenuContainer,
  DefaultClose,
  RankingCard,
} from "../../components";
import {
  BATTLE_ICON,
  CLOSE_ICON_BLACK,
  TRAINING_ICON,
  TROPHY,
  EXIT_ACCOUNT,
} from "../../../assets";
import { useGlobalUser } from "../../../context";

export function DefaultMenu({ currentUrl, buttonStateCallback, user }) {
  const { push } = useHistory();
  const [, setUser] = useGlobalUser();

  function handleSelectPage(selectedPath) {
    push(selectedPath);
  }

  function handleLogout() {
    push(PATH.LOGIN);
    setUser("");
  }

  function handleCloseMenu() {
    buttonStateCallback();
  }

  function checkMenuButton() {
    if (buttonStateCallback !== undefined) {
      return (
        <DefaultClose handleClose={handleCloseMenu} img={CLOSE_ICON_BLACK} />
      );
    }
  }

  return (
    <MenuContainer>
      <div className="default-menu__main">
        <div className="default-menu__header">
          <RankingCard name={user?.name} ranking={user?.ranking} />
          {checkMenuButton()}
        </div>

        <h3 className="default-menu__title">O que quer fazer?</h3>
        <div className="default-menu__options">
          <OptionsButton
            disabled={currentUrl === PATH.TRAINING}
            onClick={() => handleSelectPage(PATH.TRAINING)}
          >
            <img
              className="default-menu__image"
              src={TRAINING_ICON}
              alt="ícone default de usuário"
            />
            <div>
              <h3 className="default-menu__description">Treinar</h3>
              <p className="default-menu__description">
                Quero jogar sozinho para aprender
              </p>
            </div>
          </OptionsButton>
          <OptionsButton
            disabled={currentUrl === PATH.BATTLE_MENU}
            onClick={() => handleSelectPage(PATH.BATTLE_MENU)}
          >
            <img
              className="default-menu__image"
              src={BATTLE_ICON}
              alt="ícone default de usuário"
            />
            <div>
              <h3 className="default-menu__description">Competir</h3>
              <p className="default-menu__description">
                Quero jogar com outra pessoa
              </p>
            </div>
          </OptionsButton>
          <OptionsButton
            disabled={currentUrl === PATH.RANKING}
            onClick={() => handleSelectPage(PATH.RANKING)}
          >
            <img
              className="default-menu__image"
              src={TROPHY}
              alt="ícone de um troféu"
            />
            <div>
              <h3 className="default-menu__description">Os Melhores</h3>
              <p className="default-menu__description">
                Ver jogadores com mais pontos
              </p>
            </div>
          </OptionsButton>
          <OptionsButton onClick={handleLogout}>
            <img
              className="default-menu__image"
              src={EXIT_ACCOUNT}
              alt="ícone para sair da conta do usuário"
            />
            <p className="default-menu__description-small">
              Deslogar do Flex-Battle
            </p>
          </OptionsButton>
        </div>
      </div>
    </MenuContainer>
  );
}
