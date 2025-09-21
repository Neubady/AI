using System.Collections;
using System.Threading.Tasks;
using UnityEngine;
using PongRoyale.Core;
using PongRoyale.Services.Player;
using PongRoyale.Services.Economy;
using PongRoyale.Gameplay.Pong.Config;

namespace PongRoyale.Gameplay.Pong
{
    /// <summary>
    /// Coordinates a single-player practice session against the AI. It listens for the
    /// match end event and rewards the player with XP and soft currency to validate the
    /// progression pipeline.
    /// </summary>
    public class PracticeModeController : MonoBehaviour
    {
        [SerializeField]
        private PongGameManager gameManager = default!;

        [SerializeField]
        private int victoryXpReward = 20;

        [SerializeField]
        private int defeatXpReward = 5;

        [SerializeField]
        private int pointsReward = 10;

        [SerializeField]
        private int softCurrencyReward = 15;

        private IPlayerProfileService _profileService = default!;
        private IEconomyService _economyService = default!;

        private void Awake()
        {
            _profileService = ServiceLocator.Resolve<IPlayerProfileService>();
            _economyService = ServiceLocator.Resolve<IEconomyService>();
        }

        public void ApplyConfig(PongGameConfig config)
        {
            victoryXpReward = config.victoryXpReward;
            defeatXpReward = config.defeatXpReward;
            pointsReward = config.pointsReward;
            softCurrencyReward = config.softCurrencyReward;
        }

        private void OnEnable()
        {
            gameManager.MatchFinished += OnMatchFinished;
            gameManager.ScoreUpdated += OnScoreUpdated;
        }

        private void OnDisable()
        {
            gameManager.MatchFinished -= OnMatchFinished;
            gameManager.ScoreUpdated -= OnScoreUpdated;
        }

        private void Start()
        {
            gameManager.ResetMatch();
        }

        private async void OnMatchFinished(PlayerSide winner)
        {
            var victory = winner == PlayerSide.Left;
            var xp = victory ? victoryXpReward : defeatXpReward;
            _profileService.AddExperience(xp);
            _profileService.RecordMatchResult(victory, pointsReward);

            await _economyService.AddSoftCurrencyAsync(softCurrencyReward);

            StartCoroutine(RestartRoutine());
        }

        private IEnumerator RestartRoutine()
        {
            yield return new WaitForSeconds(2f);
            gameManager.ResetMatch();
        }

        private void OnScoreUpdated(int leftScore, int rightScore)
        {
            // Hook for future UI updates. For now it simply logs the score.
            Debug.Log($"Score: {leftScore} - {rightScore}");
        }
    }
}
