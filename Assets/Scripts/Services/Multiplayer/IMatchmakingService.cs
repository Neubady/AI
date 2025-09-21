using System;
using System.Threading.Tasks;

namespace PongRoyale.Services.Multiplayer
{
    public interface IMatchmakingService
    {
        Task<MatchTicket> QueueForMatchAsync(MatchParameters parameters);
        Task CancelMatchmakingAsync(MatchTicket ticket);
    }

    public readonly struct MatchParameters
    {
        public readonly int PlayerLevel;
        public readonly int ArenaId;

        public MatchParameters(int playerLevel, int arenaId)
        {
            PlayerLevel = playerLevel;
            ArenaId = arenaId;
        }
    }

    public readonly struct MatchTicket
    {
        public readonly Guid TicketId;

        public MatchTicket(Guid ticketId)
        {
            TicketId = ticketId;
        }
    }
}
