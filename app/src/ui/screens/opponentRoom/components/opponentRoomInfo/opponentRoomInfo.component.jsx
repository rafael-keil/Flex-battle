import { useState } from "react";
import "./opponentRoomInfo.style.css";
import {
  DefaultInput,
  MenuContainer,
  DefaultButton,
  Header,
} from "../../../../components";
import { BATTLE_ICON } from "../../../../../assets";

export function OpponentRoomInfo({ handleOpponentRoomSubmit }) {
  const [token, setToken] = useState("");

  function handleOwnerRoom(event) {
    event.preventDefault();
    handleOpponentRoomSubmit(token);
  }

  return (
    <main className="owner-room-info">
      <Header img={BATTLE_ICON} menuButton={true} />
      <MenuContainer>
        <h2 className="opponent-room-info__title">
          Insira o código da sala abaixo:
        </h2>

        <div className="opponent-room-info__input">
          <DefaultInput
            onChange={setToken}
            value={token}
            label="Código da sala"
            type="text"
          />
        </div>

        <DefaultButton onClick={handleOwnerRoom}>
          Enviar Solicitação
        </DefaultButton>
      </MenuContainer>
    </main>
  );
}
