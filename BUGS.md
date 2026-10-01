# Bug Reports: SauceDemo `problem_user`

SauceDemo includes a `problem_user` account with bugs built in on purpose, so testers can practice finding and reporting them.
I tested this account against the behavior of `standard_user` and wrote up each defect below.

Each bug has an automated check in [`ProblemUserBugsTest`](src/test/java/tests/ProblemUserBugsTest.java).
Those tests assert the **correct** behavior, so they are **expected to fail** while the bug exists.
They run in a separate suite so they don't break the regression build:

```
mvn test -Dsuite=testng-known-bugs.xml
```

| ID | Summary | Severity | Automated check |
|----|---------|----------|-----------------|
| BUG-01 | All products show the same wrong image | Medium | `productImagesShouldBeDifferent` |
| BUG-02 | Sort dropdown does not reorder products | Medium | `sortingShouldReorderProducts` |
| BUG-03 | "Add to cart" does nothing for some products | High | `everyProductShouldBeAddableToCart` |
| BUG-04 | Clicking a product opens a different product | High | `productLinkShouldOpenMatchingProduct` |
| BUG-05 | Last Name field on checkout cannot be filled in | Critical | `lastNameFieldShouldKeepTypedText` |

---

### BUG-01: All products show the same wrong image

**Environment:** https://www.saucedemo.com, Chrome, user `problem_user`
**Steps to reproduce**
1. Log in as `problem_user` / `secret_sauce`.
2. Look at the product images on the Products page.

**Expected:** Each of the 6 products shows its own product photo (as it does for `standard_user`).
**Actual:** Every product shows the same placeholder image of a dog.
**Severity:** Medium. Customers cannot see what they are buying.

---

### BUG-02: Sort dropdown does not reorder products

**Steps to reproduce**
1. Log in as `problem_user`.
2. Choose **Name (Z to A)** from the sort dropdown.

**Expected:** Products are listed Z to A, starting with "Test.allTheThings() T-Shirt (Red)".
**Actual:** The list does not change; "Sauce Labs Backpack" stays first. The same happens with both price sorts.
**Severity:** Medium. A core browsing feature does not work.

---

### BUG-03: "Add to cart" does nothing for some products

**Steps to reproduce**
1. Log in as `problem_user`.
2. Click **Add to cart** on all 6 products.

**Expected:** The cart icon shows 6.
**Actual:** The cart icon shows 3. Sauce Labs Bolt T-Shirt, Sauce Labs Fleece Jacket and Test.allTheThings() T-Shirt (Red) are not added, and their buttons do not change to **Remove**.
**Severity:** High. Customers cannot buy half of the catalog.

---

### BUG-04: Clicking a product opens a different product

**Steps to reproduce**
1. Log in as `problem_user`.
2. Click the product name **Sauce Labs Backpack**.

**Expected:** The details page for Sauce Labs Backpack opens.
**Actual:** The details page for a different product (Sauce Labs Fleece Jacket) opens.
**Severity:** High. Customers may buy the wrong item.

---

### BUG-05: Last Name field on checkout cannot be filled in

**Steps to reproduce**
1. Log in as `problem_user` and add **Sauce Labs Bike Light** to the cart.
2. Open the cart and click **Checkout**.
3. Type `Jane` in First Name, then `Doe` in Last Name.

**Expected:** First Name shows "Jane" and Last Name shows "Doe".
**Actual:** Last Name stays empty, and the letters typed into Last Name overwrite First Name instead. Clicking **Continue** shows "Error: Last Name is required".
**Severity:** Critical. The user cannot complete checkout, so no order can be placed.
