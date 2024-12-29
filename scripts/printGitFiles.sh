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

# Get the path to the project root directory
PROJECT_ROOT=$(git rev-parse --show-toplevel)

# Get the path to the build/outputs directory
OUTPUTS_DIR="$PROJECT_ROOT/build/outputs"

# Create the outputs directory if it doesn't exist
mkdir -p "$OUTPUTS_DIR"

# Change to the project root directory
cd "$PROJECT_ROOT"

# Get the list of all tracked files in Git with the specified extensions
TRACKED_FILES=$(git ls-files -- '*\.kt' '*\.kts' '*\.properties' '*\.yml' '*\.sh')

# Open the output file for writing
OUTPUT_FILE="$OUTPUTS_DIR/git_tracked_files.txt"
> "$OUTPUT_FILE"  # Clear the contents of the output file

# Loop through the tracked files and write their contents to the output file
for file in $TRACKED_FILES; do
    full_path="$PROJECT_ROOT/$file"

    # Check if the file exists
    if [ -f "$full_path" ]; then
        echo "File: $file" >> "$OUTPUT_FILE"
        cat "$full_path" >> "$OUTPUT_FILE"
        echo >> "$OUTPUT_FILE"  # Add a blank line between files
    else
        echo "File not found: $file" >> "$OUTPUT_FILE"
    fi
done
