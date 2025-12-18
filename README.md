A continuación se muestran **todos los endpoints** implementados el sistema:

* Reactivar: **`/activate/{id}`** (en todos los módulos)
* StockByVariant: **`/set-quantity/{id}`** y **`/adjust/{id}`**

---

## 1) Categories — `/api/v1/categories`

* **POST** `/api/v1/categories`
* **GET** `/api/v1/categories`
* **GET** `/api/v1/categories/{id}`
* **PUT** `/api/v1/categories/{id}`
* **DELETE** `/api/v1/categories/{id}`  *(eliminación lógica)*
* **PATCH** `/api/v1/categories/activate/{id}`  *(reactivar)*

---

## 2) Measures — `/api/v1/measures`

* **POST** `/api/v1/measures`
* **GET** `/api/v1/measures`
* **GET** `/api/v1/measures/{id}`
* **PUT** `/api/v1/measures/{id}`
* **DELETE** `/api/v1/measures/{id}` *(eliminación lógica)*
* **PATCH** `/api/v1/measures/activate/{id}` *(reactivar)*

---

## 3) Colors — `/api/v1/colors`

* **POST** `/api/v1/colors`
* **GET** `/api/v1/colors`
* **GET** `/api/v1/colors/{id}`
* **PUT** `/api/v1/colors/{id}`
* **DELETE** `/api/v1/colors/{id}` *(eliminación lógica)*
* **PATCH** `/api/v1/colors/activate/{id}` *(reactivar)*

---

## 4) Warehouses — `/api/v1/warehouses`

* **POST** `/api/v1/warehouses`
* **GET** `/api/v1/warehouses`
* **GET** `/api/v1/warehouses/{id}`
* **PUT** `/api/v1/warehouses/{id}`
* **DELETE** `/api/v1/warehouses/{id}` *(eliminación lógica)*
* **PATCH** `/api/v1/warehouses/activate/{id}` *(reactivar)*

---

## 5) Products — `/api/v1/products`

* **POST** `/api/v1/products`
* **GET** `/api/v1/products`

    * Filtro opcional: `?categoryId={categoryId}`
* **GET** `/api/v1/products/{id}`
* **PUT** `/api/v1/products/{id}`
* **DELETE** `/api/v1/products/{id}` *(eliminación lógica)*
* **PATCH** `/api/v1/products/activate/{id}` *(reactivar)*

---

## 6) ProductMeasures — `/api/v1/product-measures`

* **POST** `/api/v1/product-measures`
* **GET** `/api/v1/product-measures`

    * Filtros opcionales:

        * `?productId={productId}`
        * `?measureId={measureId}`
        * `?productId={productId}&measureId={measureId}`
* **GET** `/api/v1/product-measures/{id}`
* **PUT** `/api/v1/product-measures/{id}`
* **DELETE** `/api/v1/product-measures/{id}` *(eliminación lógica)*
* **PATCH** `/api/v1/product-measures/activate/{id}` *(reactivar)*

---

## 7) ProductColors — `/api/v1/product-colors`

* **POST** `/api/v1/product-colors`
* **GET** `/api/v1/product-colors`

    * Filtros opcionales:

        * `?productId={productId}`
        * `?colorId={colorId}`
        * `?productId={productId}&colorId={colorId}`
* **GET** `/api/v1/product-colors/{id}`
* **PUT** `/api/v1/product-colors/{id}`
* **DELETE** `/api/v1/product-colors/{id}` *(eliminación lógica)*
* **PATCH** `/api/v1/product-colors/activate/{id}` *(reactivar)*

---

## 8) StockByVariant — `/api/v1/stock`

### CRUD + lógica

* **POST** `/api/v1/stock`
* **GET** `/api/v1/stock`

    * Filtros opcionales:

        * `?warehouseId={warehouseId}`
        * `?productId={productId}`
        * `?productId={productId}&warehouseId={warehouseId}`
        * `?productId={productId}&measureId={measureId}&colorId={colorId}&warehouseId={warehouseId}`
* **GET** `/api/v1/stock/{id}`
* **PUT** `/api/v1/stock/{id}`
* **DELETE** `/api/v1/stock/{id}` *(eliminación lógica)*
* **PATCH** `/api/v1/stock/activate/{id}` *(reactivar)*

### Operaciones de stock (según su cambio)

* **PATCH** `/api/v1/stock/set-quantity/{id}` *(set cantidad exacta)*
* **PATCH** `/api/v1/stock/adjust/{id}` *(incrementar/decrementar)*

### Disponibilidad (frontend)

* **GET** `/api/v1/stock/availability?productId={p}&measureId={m}&colorId={c}` *(suma en almacenes activos)*
* **GET** `/api/v1/stock/availability/by-warehouse?productId={p}&measureId={m}&colorId={c}&warehouseId={w}` *(por almacén)*

---

Se trabajo todo el sistema en el siguiente orden:

- Categories ✅ (este mensaje)

- Measures

- Colors

- Warehouses

- Products (depende de Categories)

- ProductMeasures (depende de Products + Measures)

- ProductColors (depende de Products + Colors)

- StockByVariant (depende de Products + Measures + Colors + Warehouses)

- Finalmente se trabajo con el cloudinary (subir imágenes)