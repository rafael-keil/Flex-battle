import { useState } from "react";
import { BATTLE_ICON } from "../../../../../assets";
import {
  DefaultButton,
  Header,
  MenuContainer,
  Snackbar,
} from "../../../../components";
import "./ownerRoomInfo.style.css";

export function OwnerRoomInfo({ token }) {
  const [copied, setCopied] = useState(false);

  function handleCopy() {
    navigator.clipboard.writeText(token);
    setCopied(true);
  }

  function handleClose() {
    setCopied(false);
  }

  return (
    <div className="owner-room-info">
      <Header img={BATTLE_ICON} menuButton={true} />

      <MenuContainer>
        <h2 className="owner-room-info__title">Criar Sala</h2>
        <p className="owner-room-info__description">
          Disponibilize esse código para o oponente:
        </p>

        <div className="owner-room-info__token-container">
          <p className="owner-room-info__token">{token}</p>
          <DefaultButton onClick={handleCopy}>copiar</DefaultButton>
        </div>

        <p className="owner-room-info__secondary-description">
          Esperando adversário...
        </p>
      </MenuContainer>

      <Snackbar
        text="Código copiado!"
        visibility={copied}
        handleClose={handleClose}
        type="success"
      />
    </div>
  );
}
