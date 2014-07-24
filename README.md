# social-centrality

An utility for calculating closeness centrality metric for social graphs

## System Requirements

- Java
- Leiningen (installation instructions at: http://leiningen.org/#install)

## Usage

With an edges file as input (output will be written to '/home/gustavo/edges_closeness') :

    lein run /home/gustavo/edges

With a facebook user token (output will be written to 'fbtoken:<token>_closeness')

    lein run fbtoken:<facebook_user_token>

Obs: When using Facebook, the graph will be built based on the user friend list and the mutual friends for each friend

## Tests
To run the suite:

    lein test

Tests are located at the 'test' folder


