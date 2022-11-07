export function kebabCaseStringIntoObjectCamelCase(input) {
  const inputLines = input.split(";");

  return inputLines.reduce((acum, line) => {
    const splitedLine = line.split(":");

    if (splitedLine[1]) {
      return {
        ...acum,
        [splitedLine[0].trim().replace(/-./g, (x) => x.toUpperCase()[1])]:
          splitedLine[1].trim(),
      };
    }
    return { ...acum };
  }, {});
}
