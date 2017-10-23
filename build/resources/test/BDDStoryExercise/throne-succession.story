Narrative:
    As a king's clerk
    I want to have a tool to ease my work in terms defining throne successor
    So that I won't be beheaded for wrong decision


Scenario: Oldest "King's Son" inherits the throne
Given Royal Family Exists
When  "King's Son" is Alive
And   King Died
Then  Oldest "King's Son" inherits the throne

Scenario: Oldest "King's Daughter" inherits the throne
Given Royal Family Exists
When  King's Son is non existent, illegitimate or dead
And   King's Daughter is Alive
And   King Died
Then  Oldest King's Daughter inherits the throne

Scenario: Queen inherits the throne
Given Royal Family Exists
When  Queen is Alive and have rights to throne
And   King's Son is non existent, illegitimate or dead
And   King's Daughter is non existent, illegitimate or dead
And   King abdicated before death And choose Queen as a next King
Then  Queen inherits the throne

Scenario: Royal Dynasty ends due to no succesors
Given Royal Family Exists
And   There's no other Relatives with rights to throne
When  King's Son is non existent, illegitimate or dead
And   King's Daughter is non existent, illegitimate or dead
And   Queen is Dead
And   King Died
Then  Royal dynasty ends


Scenario: Royal Dynasty ends due to all successors being illegitimate and not having rights to throne
Given Royal Family Exists
And   There's no other Relatives with rights to throne
When  King's Sons are all illegitimate
And   King's Daughter are all illegitimate
And   King Died
Then  Royal dynasty ends

Scenario: Test of Royal Succession when King didn't left children
Given Royal Family Exists
When  King have a Son And a Daughter
And   King's Son have two Sons and a Daughter
And   King Died
Then  Oldest "King's Son" inherits the throne
When  King Died
Then  Second "King's First Son" inherits the throne
When  King Died
Then  Second "King's Second Son" inherits the throne
When  King Died
Then  Oldest King's Daughter inherits the throne
Then  Succession History should show list of King-King-King-Queen
When  King Died
Then  First King's Daughter inherits the throne


Scenario: Only legitimate child lost rights to throne
Given Royal Family Exists
When  King have a Son And a Daughter
And   Royal Son loses rights to throne
And   Royal Daughter loses rights to throne
When  King Died
Then  Royal dynasty ends

Scenario: King should be dead on his dying bed, but die after his lifespan ends
Given Royal Family Exists
When  Last King is at his bed of death
Then  Last King is Alive
When  Last King exceeds his lifespan
Then  Royal dynasty ends

Scenario: Test in Game annoucements functionality
Given Royal Family Exists
Then  Royal Announcement works

Scenario: Test Kingdom Time passing functionality
Given Royal Family Exists
When  100 years passed
Then  Kingdom Time Changed

Scenario: Test methods in random scenario
Given Royal Family Exists
Then  Random User executing methods randomly shouldn't find any errors

Scenario: Test each method at least once
Given Royal Family Exists
Then  Random User is executing all methods once in sequence shouldn't find any errors

Scenario: King don't have Heir, but queen is dead. King then marry woman that gave him Heir.
Given Royal Family Exists
And   King is Alive
And   Queen is Alive
And   There's no other Relatives with rights to throne
When  Queen is Dead
And   Two people are generated as random royal family members
Then  King have one child
And   Queen is Alive

Scenario: Princess mary someone, that someone don't have rights to throne by default
Given Royal Family Exists
When  King's Son is non existent, illegitimate or dead
And   King's Daughter is Alive
And   New Human Fiance is created
And   King's Daughter is married to Fiance
Then  King's Daughter is married
And   King's Daughter Husband do not have rights to throne