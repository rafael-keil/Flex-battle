import { useEffect, useState } from "react";
import { useHistory, useLocation } from "react-router";
import { useGlobalUser } from "../../../context";
import { useFbApi } from "../../../hooks";
import {
  MenuContainer,
  DefaultButton,
  DefaultInput,
  Loading,
  Snackbar,
  Header,
} from "../../components";
import "./login.style.css";
import { LOGO_CWI } from "../../../assets";

export function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");
  const [, setUser] = useGlobalUser();
  const fbApi = useFbApi();
  const { state } = useLocation();
  const { replace } = useHistory();

  useEffect(() => {
    if (state?.invalidToken) {
      setError("Token expirado!");
    }
    replace({ ...state, invalidToken: null });
  }, []);

  async function handleLoginSubmit(event) {
    event.preventDefault();

    setIsLoading(true);

    try {
      const user = await fbApi.login(username, password);

      setUser(user);
    } catch {
      setIsLoading(false);
      setError("Usuário ou senha incorretos!");
    }
  }

  function handleClose() {
    setError("");
  }

  return (
    <div className="login">
      <Header />
      <MenuContainer>
        <div className="login__header">
          <img src={LOGO_CWI} alt="logo da empresa CWI" />
          <div className="login__header-div"></div>
          <div className="login__header-information">
            <h2 className="login__subtitle">Login</h2>
            <p className="login__header-information-detail">
              Faça login com o seu usuário e senha da CWI
            </p>
          </div>
        </div>

        <form onSubmit={handleLoginSubmit}>
          <div className="login__input">
            <DefaultInput
              onChange={setUsername}
              value={username}
              type="text"
              label="Usuário"
            />
            <DefaultInput
              onChange={setPassword}
              value={password}
              type="password"
              label="Senha"
            />
          </div>

          <a
            className="login__forgot-password"
            href="https://cwi.com.br/esqueci-a-senha/"
          >
            Esqueci minha senha
          </a>

          <DefaultButton>Entrar</DefaultButton>
        </form>
      </MenuContainer>

      <Loading isLoading={isLoading} />
      <Snackbar
        text={error}
        visibility={error}
        handleClose={handleClose}
        type="error"
      />
    </div>
  );
}
