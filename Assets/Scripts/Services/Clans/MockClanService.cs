using System.Collections.Generic;
using System.Threading.Tasks;
using UnityEngine;

namespace PongRoyale.Services.Clans
{
    [CreateAssetMenu(menuName = "Pong Royale/Services/Mock Clan Service")]
    public class MockClanService : ScriptableObject, IClanService
    {
        [SerializeField]
        private List<ClanSummary> joinedClans = new();

        public Task<IReadOnlyList<ClanSummary>> GetJoinedClansAsync()
        {
            IReadOnlyList<ClanSummary> result = joinedClans.AsReadOnly();
            return Task.FromResult(result);
        }

        public Task<ClanSummary> CreateClanAsync(string clanName)
        {
            var clan = new ClanSummary($"clan-{joinedClans.Count + 1}", clanName, 1);
            joinedClans.Add(clan);
            return Task.FromResult(clan);
        }

        public Task JoinClanAsync(string clanId)
        {
            return Task.CompletedTask;
        }
    }
}
