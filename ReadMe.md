## Ticketing System


# Assumptions:
- There are two type of user: customer, agent.
- Customer can only create ticket.
- Agent can update the tickets.
- Closing of ticket only happen by cron job.
- Agent are more of static which mean adding or removing agent are rare to happen.
- Any agent can access any customer ticket.


# Expectations:
- Any agent should be able to create tickets.
- For agent response, email should be triggered.
- Agent should be assigned automatically(should be evenly distributed).


## API's collection link: https://www.getpostman.com/collections/b607f2466d6f5addfea1
