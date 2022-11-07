import "./menu.style.css";
import { DefaultMenu, Header, Loading } from "../../components";
import { useFbApi } from "../../../hooks";
import { useEffect, useState } from "react";

export function Menu() {
  const [user, setUser] = useState();
  const [isLoading, setIsLoading] = useState(true);
  const fbApi = useFbApi();

  useEffect(() => {
    async function getUser() {
      try {
        const newUser = await fbApi.getUser();

        setUser(newUser);
      } catch {}

      setIsLoading(false);
    }

    getUser();
  }, [fbApi]);

  return (
    <div className="menu__container">
      <Header />
      <DefaultMenu user={user} />

      <Loading isLoading={isLoading} />
    </div>
  );
}
