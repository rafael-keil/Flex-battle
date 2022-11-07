import { useMemo } from "react";
import { useHttp } from "../_base/use-http";
import { useGlobalUser } from "../../../context";

export function useFbApi() {
  const [user] = useGlobalUser();

  const httpInstance = useHttp(process.env.REACT_APP_API_URL, {
    authorization: user,
  });

  async function login(username, password) {
    return await httpInstance.post("/public/user/login", {
      username,
      password,
    });
  }

  async function getUser() {
    return await httpInstance.get("/private/user");
  }

  async function geRanking() {
    const response = await httpInstance.get("/private/user/leaderboard");

    return response.content;
  }

  async function getRandomChallenge() {
    return await httpInstance.get("/private/challenge");
  }

  async function getToken() {
    return await httpInstance.get("/private/battle/token");
  }

  async function enterBattle(room) {
    return await httpInstance.put(`/private/battle/${room}/enter`);
  }

  async function getStartBattleChallenges(room) {
    return await httpInstance.get(`/private/battle/${room}/start`);
  }

  async function inputOpponentBattle(room, input) {
    return await httpInstance.post(`/private/battle/${room}/opponent/input`, {
      input: input,
    });
  }

  async function inputOwnerBattle(room, input) {
    return await httpInstance.post(`/private/battle/${room}/owner/input`, {
      input: input,
    });
  }

  async function getNewOpponentChallenge(room) {
    return await httpInstance.get(`/private/battle/${room}/opponent/challenge`);
  }

  async function getNewOwnerChallenge(room) {
    return await httpInstance.get(`/private/battle/${room}/owner/challenge`);
  }

  async function getNewOpponentPoints(room) {
    return await httpInstance.get(`/private/battle/${room}/opponent/points`);
  }

  async function getNewOwnerPoints(room) {
    return await httpInstance.get(`/private/battle/${room}/owner/points`);
  }

  async function getWinner(room) {
    return await httpInstance.get(`/private/battle/${room}/winner`);
  }

  return useMemo(
    () => ({
      login,
      getUser,
      geRanking,
      getRandomChallenge,
      getToken,
      enterBattle,
      getStartBattleChallenges,
      inputOpponentBattle,
      inputOwnerBattle,
      getNewOpponentChallenge,
      getNewOwnerChallenge,
      getNewOpponentPoints,
      getNewOwnerPoints,
      getWinner,
    }),
    [user]
  );
}
