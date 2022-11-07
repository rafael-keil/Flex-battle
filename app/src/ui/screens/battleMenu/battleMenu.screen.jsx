import "./battleMenu.style.css"
import { PATH } from "../../../constants"
import { useHistory } from "react-router"
import { OptionsButton, MenuContainer, Header } from "../../components"
import { BATTLE_ICON, OWNER_ROOM, OPPONENT_ROOM } from "../../../assets"

export function BattleMenu() {
  const { push } = useHistory()

  return (
    <main className="battle-menu">
      <Header
        img={BATTLE_ICON}
        menuButton={true}
        currentUrl={PATH.BATTLE_MENU}
      />

      <MenuContainer>
        <div className="battle-menu__main">
          <h2 className="battle-menu__title">O que quer fazer?</h2>
          <div className="battle-menu__options">
            <OptionsButton onClick={() => push(PATH.BATTLE_CREATE)}>
              <img
                className="battle-menu__image"
                src={OWNER_ROOM}
                alt="ícone criar sala"
              />
              <div>
                <h3 className="battle-menu__description">Criar Sala</h3>
                <p className="battle-menu__description">Criar nova sala</p>
              </div>
            </OptionsButton>
            <OptionsButton onClick={() => push(PATH.BATTLE_ENTER)}>
              <img
                className="battle-menu__image"
                src={OPPONENT_ROOM}
                alt="ícone entrar sala"
              />
              <div>
                <h3 className="battle-menu__description">Entrar em Sala</h3>
                <p className="battle-menu__description">
                  Entrar em uma sala já existente
                </p>
              </div>
            </OptionsButton>
          </div>
        </div>
      </MenuContainer>
    </main>
  )
}
