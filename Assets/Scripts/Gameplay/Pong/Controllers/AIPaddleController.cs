using UnityEngine;

namespace PongRoyale.Gameplay.Pong.Controllers
{
    /// <summary>
    /// Simple AI controller that attempts to follow the ball with a configurable
    /// reaction delay to keep the practice mode approachable.
    /// </summary>
    public class AIPaddleController : PaddleControllerBase
    {
        [SerializeField]
        private Transform ballTransform = default!;

        [SerializeField, Range(0f, 1f)]
        private float reactionSpeed = 0.65f;

        private float _targetY;

        protected override void Awake()
        {
            base.Awake();
            _targetY = Body.position.y;

            if (ballTransform == null)
            {
                Debug.LogError("Ball transform must be assigned to the AI paddle controller.");
            }
        }

        private void Update()
        {
            if (ballTransform == null)
            {
                return;
            }

            _targetY = Mathf.Lerp(_targetY, ballTransform.position.y, reactionSpeed * Time.deltaTime * 10f);
        }

        protected override float GetTargetPosition()
        {
            return _targetY;
        }
    }
}
