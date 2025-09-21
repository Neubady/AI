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
    /// Responsible for registering core services required by the rest of the project.
    /// It should be placed in the first scene that loads when the app starts (e.g. a
    /// bootstrap scene).
    /// </summary>
    public class GameBootstrapper : MonoBehaviour
    {
        [SerializeField]
        private ScriptableServiceConfig serviceConfig = default!;

        private void Awake()
        {
            ServiceLocator.Clear();

            if (serviceConfig == null)
            {
                Debug.LogWarning("Service configuration is missing. Using in-memory mock services.");
                RegisterFallbackServices();
                return;
            }

            serviceConfig.RegisterAllServices();
        }

        private void RegisterFallbackServices()
        {
            ServiceLocator.Register<IPlayerProfileService>(ScriptableObject.CreateInstance<InMemoryPlayerProfileService>());
            ServiceLocator.Register<IMatchmakingService>(ScriptableObject.CreateInstance<MockMatchmakingService>());
            ServiceLocator.Register<IClanService>(ScriptableObject.CreateInstance<MockClanService>());
            ServiceLocator.Register<IEconomyService>(ScriptableObject.CreateInstance<MockEconomyService>());
            ServiceLocator.Register<ICloudSaveService>(ScriptableObject.CreateInstance<MockCloudSaveService>());
            ServiceLocator.Register<IIapService>(ScriptableObject.CreateInstance<MockIapService>());
        }
    }
}
