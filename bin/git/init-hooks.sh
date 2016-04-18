#!/bin/sh

# When you add a git hook to the hooks folder, remember to add it here
HOOK_NAMES="pre-push"
# Get actual git hooks folder
HOOK_DIR=$(git rev-parse --show-toplevel)/.git/hooks

for hook in $HOOK_NAMES; do
  # If the hook exists and isn't a symlink, move to .local file
  if [ ! -h $HOOK_DIR/$hook ] && [ -f $HOOK_DIR/$hook ]; then
    mv $HOOK_DIR/$hook $HOOK_DIR/$hook.local
  fi

  # Create symlink to file
  ln -s -f ../../bin/git/hooks/$hook $HOOK_DIR/$hook
done
