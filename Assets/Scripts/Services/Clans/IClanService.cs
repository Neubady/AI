using System.Collections.Generic;
using System.Threading.Tasks;

namespace PongRoyale.Services.Clans
{
    public interface IClanService
    {
        Task<IReadOnlyList<ClanSummary>> GetJoinedClansAsync();
        Task<ClanSummary> CreateClanAsync(string clanName);
        Task JoinClanAsync(string clanId);
    }

    public readonly struct ClanSummary
    {
        public readonly string ClanId;
        public readonly string ClanName;
        public readonly int MemberCount;

        public ClanSummary(string clanId, string clanName, int memberCount)
        {
            ClanId = clanId;
            ClanName = clanName;
            MemberCount = memberCount;
        }
    }
}
