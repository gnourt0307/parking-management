// Common Modal Functions for Admin Pages
function openAddModal() {
    const addModal = document.getElementById('addModal');
    if (addModal)
        addModal.style.display = 'block';
}

function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal)
        modal.style.display = 'none';
}

function openEditModal(...args) {
    if (document.getElementById('editSlotID')) {
        // Slot editing
        const [id, name, zone, type, status] = args;
        document.getElementById('editSlotID').value = id;
        document.getElementById('editSlotName').value = name;
        document.getElementById('editZoneID').value = zone;
        document.getElementById('editTypeID').value = type;
        document.getElementById('editStatus').value = status;
    } else if (document.getElementById('editZoneID')) {
        // Zone editing
        const [id, name, typeName, desc] = args;
        document.getElementById('editZoneID').value = id;
        document.getElementById('editZoneName').value = name;
        document.getElementById('editVehicleTypeName').value = typeName;
        document.getElementById('editDescription').value = desc !== 'null' && desc !== '' ? desc : '';
    } else if (document.getElementById('editPricing')) {
        const [typeName] = args;
        document.getElementById('vehicleType').value = typeName;
    } else if (document.getElementById('editUser')) {
        
    }

    const editModal = document.getElementById('editModal');
    if (editModal)
        editModal.style.display = 'block';
}

function openDetailModal(...args) {
    if (document.getElementById('detailSlotName')) {
        // Slot details
        const [name, zone, status, plate, owner, phone, time] = args;
        document.getElementById('detailSlotName').textContent = name;
        document.getElementById('detailZoneName').textContent = zone;
        document.getElementById('detailStatus').textContent = status ? status.toLowerCase() : '';

        document.getElementById('detailLicensePlate').textContent = plate ? plate : 'N/A';
        document.getElementById('detailOwnerName').textContent = owner ? owner : 'N/A';
        document.getElementById('detailOwnerPhone').textContent = phone ? phone : 'N/A';
        document.getElementById('detailEntryTime').textContent = time ? time.substring(0, 19) : 'N/A';

        // Toggle owner info visiblity based on occupied status
        // the original element only existed in slot_list.jsp
        const ownerInfo = document.getElementById('detailOwnerInfo');
        const emptyInfo = document.getElementById('detailEmptyInfo');
        if (ownerInfo && emptyInfo) {
            if (status && status.toLowerCase() === 'occupied') {
                ownerInfo.style.display = 'block';
                emptyInfo.style.display = 'none';
            } else {
                ownerInfo.style.display = 'none';
                emptyInfo.style.display = 'block';
            }
        }
    } else if (document.getElementById('detailCapacity')) {
        // Zone details
        const [name, cap, vtypes, desc] = args;
        document.getElementById('detailZoneName').textContent = name;
        document.getElementById('detailCapacity').textContent = cap;
        document.getElementById('detailVehicleTypes').textContent = vtypes;
        document.getElementById('detailDescription').textContent = desc !== 'null' && desc !== '' ? desc : 'N/A';
    }

    const detailModal = document.getElementById('detailModal');
    if (detailModal)
        detailModal.style.display = 'block';
}

// Close modal if user clicks outside of it
window.onclick = function (event) {
    if (event.target.className === 'modal' || event.target.classList.contains('modal')) {
        event.target.style.display = 'none';
    }
}
