# PONG Royale Architecture Overview

PONG Royale is structured to make it easy to iterate on core gameplay first while
progressively integrating social and live-service features.

## Layers

- **Core** – Bootstrap and service registration. A `ScriptableServiceConfig`
  asset determines which backend implementations are active, making it easy to
  swap between mock services and live ones.
- **Gameplay** – Contains feature-centric folders. The `Pong` module currently
  holds the single-player practice implementation (ball physics, paddles,
  scoring and progression hooks).
- **Services** – Defines interfaces for multiplayer, clans, economy, cloud save,
  player profile and monetisation. ScriptableObject-based mock implementations
  allow the editor to run without server dependencies while production SDKs are
  integrated.
- **UI** – Reserved for menu and HUD prefabs. Menu navigation is expected to be
  implemented with Unity's UI Toolkit or Canvas system in later milestones.

## Feature Roadmap

1. **Practice Mode (MVP)** – Already implemented with AI opponent, XP rewards
   and score tracking.
2. **Live Multiplayer** – Integrate chosen networking stack (Photon, PlayFab or
   Unity Gaming Services). Implement matchmaking, private rooms and
   authoritative game state.
3. **Progression & Economy** – Persist player profile, arenas, rewards and store
   offers through the cloud save and economy services.
4. **Clans & Social** – Expand clan service, add chat and cooperative battles.
5. **Live Ops** – Implement daily quests, event rotations and analytics hooks.

## Scenes & Installers

Each mode uses an installer MonoBehaviour to wire ScriptableObjects with scene
components. This pattern keeps configuration data out of code and aligns with
Unity's prefab/workflow best practices.

## Testing Strategy

- **Play Mode Tests** – Validate paddle control, ball physics and scoring.
- **Edit Mode Tests** – Ensure service registration and configuration remain
  stable as features scale.

