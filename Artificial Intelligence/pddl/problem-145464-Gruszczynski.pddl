(define 
(problem blocks5)
(:domain world_of_blocks)
(:objects a b c d e)
(:INIT (clear c) (clear e) (on_floor a) (on_floor d) (on b a) (on c b) (on e d) (empty_hand))
(:goal (AND (on d b)))
)