#  The Incredible Retrospective

## Weakness

One of the most significant weaknesses we identified from Iteration 1 was improper exception handling across our application layers, particularly a breakdown in the separation of concerns between our presentation layer and business layer.

In Iteration 1, we had multiple instances where exceptions were being thrown directly from the presentation layer. This was problematic because it forced business logic; deciding when and why an error should occur, into a layer that is only supposed to collect user input and display data. The presentation layer should remain "dumb"; it should catch and display errors, not produce them. By throwing exceptions there, we blurred the boundary between layers and made the codebase significantly harder to maintain and refactor.

This issue was worsened by two related problems. First, we were using generic `RuntimeException` for every error throughout the codebase, with no custom exceptions to distinguish between different failure scenarios. This made it nearly impossible to identify what went wrong or where, since every error looked the same regardless of its cause. Secondly, we weren't enforcing exception handling in method signatures anywhere an exception could be thrown from the business layer. This led to silent failures, errors were swallowed rather than handled which made our unit tests ineffective, since they could pass even when the system was misbehaving underneath.

Together, these three weaknesses produced a system that was difficult to debug and impossible to refactor in isolation. A change in one layer shotgun surgery across all layers, undermining the architectural separation that our layered design was supposed to provide.

### Corrective Action Taken

After receiving feedback from Iteration 1, we addressed this issue in Iteration 2 through refactoring.

- [Issue 1](https://code.cs.umanitoba.ca/comp3350-winter2026/a02-g09-theincredibles/-/work_items/74)
- [Issue 2](https://code.cs.umanitoba.ca/comp3350-winter2026/a02-g09-theincredibles/-/issues/65)
- [Merge Request](https://code.cs.umanitoba.ca/comp3350-winter2026/a02-g09-theincredibles/-/merge_requests/35)

Our first step was to find every location in the presentation layer where exceptions were being thrown and move them to the appropriate layer i.e the layer where the error actually originates. The presentation layer now only catches exceptions and renders the relevant message to the user.

Next, we introduced two custom exception classes in the business layer: [`BookingException`](https://code.cs.umanitoba.ca/comp3350-winter2026/a02-g09-theincredibles/-/blob/main/app/src/main/java/com/group9/ongo/business/services/BookingException.java?ref_type=heads) and [`ValidationException`](https://code.cs.umanitoba.ca/comp3350-winter2026/a02-g09-theincredibles/-/blob/main/app/src/main/java/com/group9/ongo/business/services/BookingException.java?ref_type=heads). These replace the generic `RuntimeException` calls and allow us to immediately understand the category and context of any error at a glance. These exceptions work alongside our [`ErrorMessageConstants`](https://code.cs.umanitoba.ca/comp3350-winter2026/a02-g09-theincredibles/-/blob/main/app/src/main/java/com/group9/ongo/business/constants/ErrorMessageConstants.java?ref_type=heads) class, which centralizes all error messages and ensures consistent, identifiable output throughout the system.

Finally, we enforced exception handling by adding thrown exceptions explicitly to method signatures wherever they could occur. This eliminated silent failures and made our unit tests meaningful again, a test can no longer pass while masking an underlying error.

The result is an architecture where the business layer throws specific, meaningful exceptions and the presentation layer handles them cleanly, with each layer having a clearly defined responsibility.

### Measurable Evidence of Improvement

Following these changes, our unit tests became meaningfully more reliable. Previously, tests could pass despite unhandled exceptions being swallowed silently. After enforcing exception declarations in method signatures, any unhandled exception now causes a test to fail explicitly, surfacing bugs rather than hiding them. The refactor also allowed us to test error paths directly, we could now write tests that assert a `BookingException` or `ValidationException` is thrown under specific conditions, which was not possible with generic `RuntimeException`.

Additionally, the number of cross-layer changes required per bug fix dropped noticeably during Iteration 2. In Iteration 1, a single error-handling fix often required edits in the presentation, business, and persistence layers simultaneously. After the refactor, error-related changes were largely contained to the layer where the error originated.

## Velocity Chart

![Velocity Chart](./../Velocity%20chart.png)

Our average velocity across all three iterations is $\frac{29 + 25 + 21}{3} = 25$ story points per iteration. While there is a downward trend, this was an intentional trade-off: Iteration 2 and Iteration 3 were deliberately focused on eliminating technical debt — including the exception handling refactor described above — and improving our documentation rather than adding new features. We view this as a sign of a a better development process rather than a loss of productivity. Addressing these structural issues now positions us to move faster and more confidently in future work without the overhead of debugging a fragile system.