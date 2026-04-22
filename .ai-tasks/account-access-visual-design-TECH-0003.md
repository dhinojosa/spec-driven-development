# TECH-0003 Account Access Visual Design

Governed by: Technical task

Business spec: Not applicable

Reason no Cucumber spec applies:

This task preserves the existing account registration and login behavior while
bringing the register, login, and invalid-login pages into the same visual
design language as the welcome page.

## Design System Alignment

- [ ] Reuse the welcome page palette, spacing scale, button treatment, and typography for account access pages.
- [ ] Keep the account pages visually consistent with the welcome page without cloning the markup blindly.
- [ ] Preserve responsive behavior so the first screen shows the primary form and a hint of supporting content on mobile and desktop.
- [ ] Keep border radius at `8px` or less and avoid nested card styling.
- [ ] Keep the main interaction as the first focus of the page rather than turning the screen into a marketing layout.

## Static Resources

- [ ] Refactor `full-application/src/main/resources/assets/site.css` so shared welcome/account visual rules live in reusable selectors.
- [ ] Keep feature-specific styling readable by separating shared layout rules from account-form rules.
- [ ] Reuse the existing welcome assets where they help the account pages stay visually connected.
- [ ] Add no new image asset unless the existing welcome assets are insufficient for a coherent account-access layout.

## Account Templates

- [ ] Redesign `full-application/src/main/resources/account/anonymous/register.html` to match the welcome-page visual language.
- [ ] Redesign `full-application/src/main/resources/account/anonymous/login.html` to match the welcome-page visual language.
- [ ] Redesign `full-application/src/main/resources/account/anonymous/login-invalid.html` to match the welcome-page visual language.
- [ ] Keep registration and login forms immediately visible without scrolling on common laptop and mobile viewports.
- [ ] Make the invalid-login page visibly distinct through message styling while keeping the same overall layout and navigation.
- [ ] Ensure each page has a clear path to the alternate action such as register-to-login and login-to-register.

## Controller Compatibility

- [ ] Keep `AccountHttpHandler` response behavior unchanged for register, login, and invalid-login flows.
- [ ] Verify the updated templates still work with the current form field names and form actions.
- [ ] Keep the pomodoro success page out of scope for this task unless visual drift blocks the account flow.

## Test Coverage

- [ ] Update controller tests so `GET /account/register` verifies the redesigned register page content and asset references.
- [ ] Update controller tests so `GET /account/login` verifies the redesigned login page content and asset references.
- [ ] Update controller tests so invalid login still returns the invalid page content with the new styling hooks.
- [ ] Add or update acceptance assertions only where stable visual text or asset references changed.
- [ ] Avoid brittle tests that assert CSS class names unless they encode important behavior.

## Validation

- [ ] Run `mvn verify`.
- [ ] Manually load `/`, `/account/register`, and `/account/login` to confirm the pages read as one coherent experience.
- [ ] Verify the invalid-login page still renders after a bad credential attempt.
- [ ] Verify the register and login forms remain usable on mobile-width and laptop-width viewports.
