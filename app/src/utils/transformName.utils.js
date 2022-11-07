export function transformName(name) {
  const nameSplit = name.split(".");
  const response =
    nameSplit[0].substr(0, 1) + nameSplit[nameSplit.length - 1].substr(0, 1);
  return response.toUpperCase();
}
