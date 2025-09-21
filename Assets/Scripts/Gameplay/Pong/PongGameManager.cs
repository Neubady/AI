using System;
using UnityEngine;
using PongRoyale.Gameplay.Pong.Controllers;

namespace PongRoyale.Gameplay.Pong
{
    public class PongGameManager : MonoBehaviour
    {
        [SerializeField]
        private BallController ball = default!;

        [SerializeField]
        private Transform leftGoal = default!;

        [SerializeField]
        private Transform rightGoal = default!;

        [SerializeField]
        private int pointsToWin = 7;

        private int _leftScore;
        private int _rightScore;

        public event Action<int, int>? ScoreUpdated;
        public event Action<PlayerSide>? MatchFinished;

        private void Awake()
        {
            if (ball == null)
            {
                Debug.LogError("Ball reference is missing in PongGameManager.");
            }
        }

        private void Update()
        {
            CheckForGoal();
        }

        public void Configure(int requiredPointsToWin)
        {
            pointsToWin = requiredPointsToWin;
        }

        private void CheckForGoal()
        {
            if (ball == null)
            {
                return;
            }

            if (ball.transform.position.x < leftGoal.position.x)
            {
                AwardPoint(PlayerSide.Right);
            }
            else if (ball.transform.position.x > rightGoal.position.x)
            {
                AwardPoint(PlayerSide.Left);
            }
        }

        private void AwardPoint(PlayerSide scorer)
        {
            if (scorer == PlayerSide.Left)
            {
                _leftScore++;
                ball.ResetBall(Vector2.right);
            }
            else
            {
                _rightScore++;
                ball.ResetBall(Vector2.left);
            }

            ScoreUpdated?.Invoke(_leftScore, _rightScore);

            if (_leftScore >= pointsToWin)
            {
                MatchFinished?.Invoke(PlayerSide.Left);
            }
            else if (_rightScore >= pointsToWin)
            {
                MatchFinished?.Invoke(PlayerSide.Right);
            }
        }

        public void ResetMatch()
        {
            _leftScore = 0;
            _rightScore = 0;
            ScoreUpdated?.Invoke(_leftScore, _rightScore);
            ball.ResetBall(UnityEngine.Random.value > 0.5f ? Vector2.left : Vector2.right);
        }
    }

    public enum PlayerSide
    {
        Left,
        Right
    }
}
