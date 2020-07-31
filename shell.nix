{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
  nativeBuildInputs = [
    pkgs.xmlstarlet
  ];

  shellHook = ''
    dir="$(dirname "$(realpath "$0")")"
    alias mvn="$dir/mvnw"
  '';
}
