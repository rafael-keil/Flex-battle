import _ from "lodash";

export function validadeResponse(arrayApi, input) {
  const objectApi = Object.fromEntries(arrayApi);

  return !_.isEqual(objectApi, input);
}
