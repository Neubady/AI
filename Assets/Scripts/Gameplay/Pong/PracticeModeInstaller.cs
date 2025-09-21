using UnityEngine;
using PongRoyale.Gameplay.Pong.Config;
using PongRoyale.Gameplay.Pong.Controllers;

namespace PongRoyale.Gameplay.Pong
{
    /// <summary>
    /// Applies a <see cref="PongGameConfig"/> to the runtime components in the scene to
    /// keep data-driven values outside of scripts.
    /// </summary>
    public class PracticeModeInstaller : MonoBehaviour
    {
        [SerializeField]
        private PongGameConfig config = default!;

        [SerializeField]
        private BallController ball = default!;

        [SerializeField]
        private PongGameManager gameManager = default!;

        [SerializeField]
        private PracticeModeController practiceController = default!;

        private void Awake()
        {
            if (config == null)
            {
                Debug.LogWarning("Practice mode installer missing config asset.");
                return;
            }

            if (ball != null)
            {
                ball.Configure(config.initialBallSpeed, config.ballSpeedIncreasePerHit, config.maxBallSpeed, autoLaunch: false);
            }

            if (gameManager != null)
            {
                gameManager.Configure(config.pointsToWin);
            }

            if (practiceController != null)
            {
                practiceController.ApplyConfig(config);
            }
        }
    }
}
