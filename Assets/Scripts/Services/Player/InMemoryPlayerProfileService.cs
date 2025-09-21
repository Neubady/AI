using UnityEngine;

namespace PongRoyale.Services.Player
{
    [CreateAssetMenu(menuName = "Pong Royale/Services/In Memory Player Profile")]
    public class InMemoryPlayerProfileService : ScriptableObject, IPlayerProfileService
    {
        [System.Serializable]
        private struct ArenaDefinition
        {
            public int arenaId;
            public string arenaName;
            public int requiredLevel;
        }

        [SerializeField]
        private string playerId = "player-local";

        [SerializeField]
        private string displayName = "Player";

        [SerializeField]
        private int level = 1;

        [SerializeField]
        private int currentXp = 0;

        [SerializeField]
        private int currentArena = 0;

        [SerializeField]
        private int totalVictories = 0;

        [SerializeField]
        private int totalDefeats = 0;

        [SerializeField]
        private int totalPoints = 0;

        [Header("Progression Settings")]
        [SerializeField]
        private int xpPerLevel = 100;

        [SerializeField]
        private ArenaDefinition[] arenaProgression =
        {
            new ArenaDefinition { arenaId = 0, arenaName = "Training Grounds", requiredLevel = 1 },
            new ArenaDefinition { arenaId = 1, arenaName = "Neon Arcade", requiredLevel = 3 },
            new ArenaDefinition { arenaId = 2, arenaName = "Skyline Court", requiredLevel = 6 },
            new ArenaDefinition { arenaId = 3, arenaName = "Royal Stadium", requiredLevel = 10 }
        };

        public PlayerProfileData LocalProfile => new(
            playerId,
            displayName,
            level,
            currentXp,
            currentArena,
            totalVictories,
            totalDefeats,
            totalPoints);

        public void AddExperience(int amount)
        {
            currentXp += Mathf.Max(0, amount);
            RefreshProgression();
        }

        public void RecordMatchResult(bool victory, int pointsEarned)
        {
            if (victory)
            {
                totalVictories++;
            }
            else
            {
                totalDefeats++;
            }

            totalPoints += Mathf.Max(0, pointsEarned);
        }

        private void RefreshProgression()
        {
            var computedLevel = Mathf.Max(1, 1 + currentXp / Mathf.Max(1, xpPerLevel));
            level = computedLevel;

            var highestArena = 0;
            for (var i = 0; i < arenaProgression.Length; i++)
            {
                var arena = arenaProgression[i];
                if (computedLevel >= arena.requiredLevel)
                {
                    highestArena = Mathf.Max(highestArena, arena.arenaId);
                }
            }

            currentArena = highestArena;
        }
    }
}
