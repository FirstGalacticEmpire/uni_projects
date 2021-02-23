(define (domain world_of_blocks)
  (:requirements :adl)
  (:predicates (on ?x ?y)
	       (on_floor ?x)
	       (clear ?x)
	       (empty_hand)
	       (in_hand ?x))
	       
  (:action pickup-from-floor
	     :parameters (?x)
	     :precondition (and (on_floor ?x) (clear ?x) (empty_hand))
	     :effect
	     (and 
	       (in_hand ?x)
	       (not (on_floor ?x))
		   (not (clear ?x))
		   (not (empty_hand))))

  (:action putdown-to-floor
	     :parameters (?x)
	     :precondition (in_hand ?x)
	     :effect
	     (and 
	       (not (in_hand ?x))
		   (clear ?x)
		   (empty_hand)
		   (on_floor ?x)))
		   
  (:action pickup-from-block
	     :parameters (?x ?y)
	     :precondition (and (on ?x ?y) (clear ?x) (empty_hand))
	     :effect
	     (and 
	       (in_hand ?x)
		   (clear ?y)
		   (not (clear ?x))
		   (not (empty_hand))
		   (not (on ?x ?y))))
		   
  (:action putdown-to-block
         ; This feels more intuitive:
	     ; :parameters (?x ?y)
	     
	     ; But the script on the page works with parameters in this order:
	     :parameters (?y ?x)
	     :precondition (and (in_hand ?x) (clear ?y))
	     :effect
	     (and 
	       (not (in_hand ?x))
		   (not (clear ?y))
		   (clear ?x)
		   (empty_hand)
		   (on ?x ?y))))