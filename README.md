# Object Validation Rule Manager

It is a generic object validation rule client extension able to execute rules written in groovy language.

Those rules are persisted in Liferay as Objects. The OVRM uses headless API to find the rules for a given object entry according to its objectDefinition ERC

## Benefits
* Flexibility / SaaS first. It allows custom rules to be written in groovy even in a SaaS environment, since the groovy is not executed inside Liferay there is no security breach
* Productivity. No need to write, build and deploy a new client extension for every new validation rule
* Control. It allows Admin Users to approve new rules, or to disable existing ones. Since rules are Objects they can be managed by the Liferay Workflow.

## Client extensions

- liferay-sample-etc-spring-boot
  - It is a objectValidationRule client extension. 
  - But instead of implementing the Java validation logic, it just executes validation logics written in groovy and persisted as Liferay Objects.
  - These validations blocks are retrieved via Headless API secured by OAuth2.0
- list-type-batch
  - It is a batch client extension which creates a pick list containing a demo Object Definition.
- liferay-sample-batch
  - It creates a demonstration Object to be validated

## How to use this demo

1. Copy this workspace
2. Build client-extensions
3. Deploy them in the following order
   1. spring-boot cx
   2. list-type-batch
   3. liferay-sample-batch
