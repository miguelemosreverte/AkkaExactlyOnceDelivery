package object ddd {
  
      trait Response {
        def deliveryId: BigInt
      }
  
      trait Event {
        def name: String
      }
  
      trait Query {
        def aggregateRoot: String
      }
    trait Command {
        def aggregateRoot: String
      }
  
      trait AbstractState {
        def +(event: Event): AbstractState
      }
  
    
  }
