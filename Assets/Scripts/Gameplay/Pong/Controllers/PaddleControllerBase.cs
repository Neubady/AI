using UnityEngine;

namespace PongRoyale.Gameplay.Pong.Controllers
{
    /// <summary>
    /// Base class for all paddle controllers. Handles applying velocity to the
    /// Rigidbody2D while respecting movement boundaries.
    /// </summary>
    [RequireComponent(typeof(Rigidbody2D))]
    public abstract class PaddleControllerBase : MonoBehaviour
    {
        [SerializeField]
        private float moveSpeed = 12f;

        [SerializeField]
        private Vector2 movementBounds = new(-7.5f, 7.5f);

        protected Rigidbody2D Body { get; private set; } = default!;

        protected virtual void Awake()
        {
            Body = GetComponent<Rigidbody2D>();
        }

        protected virtual void FixedUpdate()
        {
            var targetPosition = GetTargetPosition();
            var newY = Mathf.MoveTowards(Body.position.y, targetPosition, moveSpeed * Time.fixedDeltaTime);
            newY = Mathf.Clamp(newY, movementBounds.x, movementBounds.y);

            Body.MovePosition(new Vector2(Body.position.x, newY));
        }

        protected abstract float GetTargetPosition();
    }
}
