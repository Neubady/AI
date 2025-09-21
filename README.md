# PONG Royale

PONG Royale is a mobile-first Unity project that blends the classic Pong core
loop with modern live-service systems inspired by Clash Royale and Brawl Stars.
This repository contains the initial project structure and a fully playable
practice mode against an AI opponent. The architecture is prepared for
incremental integration of multiplayer, clans, store fronts and monetisation
features.

## Current Features

- Responsive touch controls for the player paddle (mouse/keyboard supported in
the editor).
- AI opponent with configurable difficulty and dynamic ball speed.
- Score tracking, match resets and progression rewards (XP, soft currency and
  stats) using in-memory services.
- ScriptableObject-driven configuration for easy tuning.

## Project Structure

```
Assets/
  Scenes/              ← Scene documentation and future Unity scenes
  Scripts/
    Core/              ← Bootstrap and service configuration
    Gameplay/Pong/     ← Pong-specific controllers and installers
    Services/          ← Service interfaces + mock implementations
  UI/                  ← Placeholder for forthcoming menu/HUD prefabs
Docs/                  ← Architecture and design documents
```

## Next Steps

1. Connect to a multiplayer backend (Photon/PlayFab) using the interfaces in
   `Assets/Scripts/Services/Multiplayer`.
2. Implement UI flows for matchmaking, player profile, clans, shop and IAP
   catalogues.
3. Persist player progression through the cloud save service and replace mock
   economy with secure server-side transactions.
4. Expand cosmetics, daily rewards and clan activities to support ongoing live
   operations.

## Requirements

- Unity 2022.3 LTS or newer.
- iOS/Android build support modules installed via Unity Hub.

