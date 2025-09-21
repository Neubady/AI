using System;
using System.Threading.Tasks;
using UnityEngine;

namespace PongRoyale.Services.Multiplayer
{
    [CreateAssetMenu(menuName = "Pong Royale/Services/Mock Matchmaking")]
    public class MockMatchmakingService : ScriptableObject, IMatchmakingService
    {
        public async Task<MatchTicket> QueueForMatchAsync(MatchParameters parameters)
        {
            await Task.Delay(TimeSpan.FromSeconds(1));
            return new MatchTicket(Guid.NewGuid());
        }

        public async Task CancelMatchmakingAsync(MatchTicket ticket)
        {
            await Task.Yield();
        }
    }
}
