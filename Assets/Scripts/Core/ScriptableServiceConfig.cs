using System;
using UnityEngine;
using PongRoyale.Services.Player;
using PongRoyale.Services.Multiplayer;
using PongRoyale.Services.Clans;
using PongRoyale.Services.Economy;
using PongRoyale.Services.Infrastructure;
using PongRoyale.Services.Monetization;

namespace PongRoyale.Core
{
    /// <summary>
    /// Scriptable object used to configure which service implementations should be
    /// registered on start up. This allows swapping between mock services and real
    /// backends without changing gameplay code.
    /// </summary>
    [CreateAssetMenu(menuName = "Pong Royale/Service Config", fileName = "ServiceConfig")]
    public class ScriptableServiceConfig : ScriptableObject
    {
        [SerializeField]
        private ScriptableObject playerProfileService = default!;

        [SerializeField]
        private ScriptableObject matchmakingService = default!;

        [SerializeField]
        private ScriptableObject clanService = default!;

        [SerializeField]
        private ScriptableObject economyService = default!;

        [SerializeField]
        private ScriptableObject cloudSaveService = default!;

        [SerializeField]
        private ScriptableObject iapService = default!;

        public void RegisterAllServices()
        {
            RegisterService<IPlayerProfileService>(playerProfileService);
            RegisterService<IMatchmakingService>(matchmakingService);
            RegisterService<IClanService>(clanService);
            RegisterService<IEconomyService>(economyService);
            RegisterService<ICloudSaveService>(cloudSaveService);
            RegisterService<IIapService>(iapService);
        }

        private static void RegisterService<TService>(ScriptableObject serviceAsset)
        {
            if (serviceAsset == null)
            {
                throw new InvalidOperationException($"Missing service asset for {typeof(TService).Name}");
            }

            if (serviceAsset is not TService implementation)
            {
                throw new InvalidOperationException($"Asset {serviceAsset.name} does not implement {typeof(TService).Name}");
            }

            ServiceLocator.Register(implementation);
        }
    }
}
