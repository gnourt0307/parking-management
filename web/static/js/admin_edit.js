document.addEventListener("DOMContentLoaded", () => {
    document.addEventListener("click", (e) => {
        const btn = e.target.closest("button");
        if (!btn)
            return;

        // Handle "Add New" button which is outside the table
        if (btn.textContent.trim().startsWith("+ Add New")) {
            const container = btn.closest(".main-content");
            if (!container)
                return;

            const table = container.querySelector(".data-table");
            if (!table)
                return;

            const tbody = table.querySelector("tbody");
            if (!tbody)
                return;

            const ths = table.querySelectorAll("thead th");
            const newTr = document.createElement("tr");

            // Infer status type based on existing text content in the table
            let statusOptions = `<input type="text" class="filter-input" value="">`;
            const tableText = tbody.textContent.toLowerCase();
            if (tableText.includes("active") || tableText.includes("inactive")) {
                statusOptions = `<select class="filter-input filter-select">
            <option value="Active">Active</option>
            <option value="Inactive">Inactive</option>
          </select>`;
            } else if (
                    tableText.includes("occupied") ||
                    tableText.includes("available")
                    ) {
                statusOptions = `<select class="filter-input filter-select">
            <option value="Available">Available</option>
            <option value="Occupied">Occupied</option>
          </select>`;
            }

            // Add form fields for each column except Actions
            for (let i = 0; i < ths.length - 1; i++) {
                const td = document.createElement("td");
                const columnName = ths[i].textContent.trim().toLowerCase();

                if (columnName === "status") {
                    td.innerHTML = statusOptions;
                } else {
                    td.innerHTML = `<input type="text" class="filter-input" value="">`;
                }
                newTr.appendChild(td);
            }

            // Append Actions column including Save and Delete
            const actionTd = document.createElement("td");
            actionTd.innerHTML = `
        <button class="btn btn-sm btn-success"><i class="fa-solid fa-floppy-disk"></i> Save</button>
        <button class="btn btn-danger btn-sm"><i class="fa-solid fa-trash"></i> Delete</button>
      `;
            newTr.appendChild(actionTd);

            tbody.appendChild(newTr);
            return;
        }

        // Now proceed with row-based buttons (Edit, Save, Delete)
        const tr = btn.closest("tr");
        if (!tr)
            return;

        if (btn.textContent.includes("Edit")) {
            const tds = tr.querySelectorAll("td");
            const table = tr.closest("table");
            const ths = table ? table.querySelectorAll("th") : [];

            // Skip the first (ID) and last (Actions) columns
            for (let i = 0; i < tds.length - 1; i++) {
                const td = tds[i];
                // Get the text content, avoiding any existing HTML structure
                const text = td.innerText.trim();

                // Keep original innerHTML on dataset to potentially restore later
                td.dataset.originalHtml = td.innerHTML;

                const columnName = ths[i]
                        ? ths[i].textContent.trim().toLowerCase()
                        : "";

                if (columnName === "status") {
                    if (text === "Active" || text === "Inactive") {
                        const isActive = text === "Active" ? "selected" : "";
                        const isInactive = text === "Inactive" ? "selected" : "";
                        td.innerHTML = `<select class="filter-input filter-select">
                <option value="Active" ${isActive}>Active</option>
                <option value="Inactive" ${isInactive}>Inactive</option>
              </select>`;
                    } else if (text === "Occupied" || text === "Available") {
                        const isOccupied = text === "Occupied" ? "selected" : "";
                        const isAvailable = text === "Available" ? "selected" : "";
                        td.innerHTML = `<select class="filter-input filter-select">
                <option value="Occupied" ${isOccupied}>Occupied</option>
                <option value="Available" ${isAvailable}>Available</option>
              </select>`;
                    } else {
                        // Default input field if status value is unrecognized
                        td.innerHTML = `<input type="text" class="filter-input" value="${text}">`;
                    }
                } else {
                    // Replace content with an input field
                    td.innerHTML = `<input type="text" class="filter-input" value="${text}">`;
                }
            }

            // Change button to Save
            btn.innerHTML = '<i class="fa-solid fa-floppy-disk"></i> Save';
            btn.classList.add("btn-success");
        } else if (btn.textContent.includes("Save")) {
            const tds = tr.querySelectorAll("td");

            for (let i = 0; i < tds.length - 1; i++) {
                const td = tds[i];
                const input = td.querySelector("input, select");
                if (input) {
                    const newValue = input.value.trim();
                    // Basic attempt to preserve span wrappers if it's a known status
                    if (
                            td.dataset.originalHtml &&
                            td.dataset.originalHtml.includes("span")
                            ) {
                        if (newValue.toLowerCase() === "occupied") {
                            td.innerHTML = `<span class="status-occupied">${newValue}</span>`;
                        } else if (newValue.toLowerCase() === "available") {
                            td.innerHTML = `<span class="status-available">${newValue}</span>`;
                        } else {
                            td.textContent = newValue;
                        }
                    } else {
                        td.textContent = newValue;
                    }
                }
            }

            // Change button back to Edit
            btn.innerHTML = '<i class="fa-solid fa-pen-to-square"></i> Edit';
            btn.classList.remove("btn-success");
        } else if (btn.textContent.includes("Delete")) {
            if (confirm("Are you sure you want to delete this record?")) {
                tr.remove();
            }
        }
    });
});
