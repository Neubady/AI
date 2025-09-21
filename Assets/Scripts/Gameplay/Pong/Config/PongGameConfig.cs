using UnityEngine;

namespace PongRoyale.Gameplay.Pong.Config
{
    [CreateAssetMenu(menuName = "Pong Royale/Pong Game Config")]
    public class PongGameConfig : ScriptableObject
    {
        [Header("Ball Settings")]
        public float initialBallSpeed = 10f;
        public float ballSpeedIncreasePerHit = 0.5f;
        public float maxBallSpeed = 20f;

        [Header("Scoring")]
        public int pointsToWin = 7;

        [Header("Rewards")]
        public int victoryXpReward = 20;
        public int defeatXpReward = 5;
        public int pointsReward = 10;
        public int softCurrencyReward = 15;
    }
}
