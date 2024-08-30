# Object Validation Rule Manager

It is a generic object validation rule client extension able to execute rules written in groovy language.

Those rules are persisted in Liferay as Objects. The OVRM uses headless API to find the rules for a given object entry according to its objectDefinition ERC

## Benefits
* Flexibility / SaaS first. It allows custom rules to be written in groovy even in a SaaS environment, since the groovy is not executed inside Liferay there is no security breach
* Productivity. No need to write, build and deploy a new client extension for every new validation rule
* Control. It allows Admin Users to approve new rules, or to disable existing ones. Since rules are Objects they can be managed by the Liferay Workflow.
