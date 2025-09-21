namespace PongRoyale.Services.Player
{
    public interface IPlayerProfileService
    {
        PlayerProfileData LocalProfile { get; }
        void AddExperience(int amount);
        void RecordMatchResult(bool victory, int pointsEarned);
    }

    public readonly struct PlayerProfileData
    {
        public readonly string PlayerId;
        public readonly string DisplayName;
        public readonly int Level;
        public readonly int CurrentXp;
        public readonly int CurrentArena;
        public readonly int TotalVictories;
        public readonly int TotalDefeats;
        public readonly int TotalPoints;

        public PlayerProfileData(
            string playerId,
            string displayName,
            int level,
            int currentXp,
            int currentArena,
            int totalVictories,
            int totalDefeats,
            int totalPoints)
        {
            PlayerId = playerId;
            DisplayName = displayName;
            Level = level;
            CurrentXp = currentXp;
            CurrentArena = currentArena;
            TotalVictories = totalVictories;
            TotalDefeats = totalDefeats;
            TotalPoints = totalPoints;
        }
    }
}
