#!/usr/bin/env bash

#
# Copyright (c) 2024 Jeffrey Nyauke
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

ROOT="$(dirname "${BASH_SOURCE[0]}")"
OS="$(uname | tr '[:upper:]' '[:lower:]')"
case $OS in
  darwin*)  echo "OSX detected" && "${ROOT}"/setupOSX.sh ;;
  linux*)   echo "Linux detected" && "${ROOT}"/setupUbuntu.sh ;;
  mingw*)   echo "Mingw detected" && "${ROOT}"/setupMingw.sh ;;
  msys*)    echo "Windows detected (unsupported)" && exit 1 ;;
  *)        echo "Unknown OS: $OS" && exit 1 ;;
esac
