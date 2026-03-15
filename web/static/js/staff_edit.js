(function () {
  const vehicleTypeSelect = document.getElementById('vehicleType');
  const slotSelect = document.getElementById('assignedSlot');
  const slotGrid = document.getElementById('slotGrid');

  if (!vehicleTypeSelect || !slotSelect || !slotGrid) {
    return;
  }

  const previewPlate = document.getElementById('previewPlate');
  const previewType = document.getElementById('previewType');
  const previewSlot = document.getElementById('previewSlot');
  const previewTime = document.getElementById('previewTime');
  const licenseInput = document.getElementById('licensePlate');

  const typeNames = {
    '1': 'Motorbike',
    '2': 'Car',
    '3': 'Bicycle',
    '4': 'Electric Car'
  };

  function formatNow() {
    const now = new Date();
    return now.toLocaleString();
  }

  function updatePreview() {
    if (!previewPlate || !previewType || !previewSlot || !previewTime || !licenseInput) {
      return;
    }
    const plate = licenseInput.value || '-';
    const typeVal = vehicleTypeSelect.value;
    const typeText = typeNames[typeVal] || '-';
    const selectedOption = slotSelect.options[slotSelect.selectedIndex];
    const zoneName = selectedOption ? selectedOption.getAttribute('data-zonename') : null;
    const slotName = selectedOption ? selectedOption.getAttribute('data-slotname') : null;

    previewPlate.textContent = plate;
    previewType.textContent = typeText;
    previewSlot.textContent = (zoneName && slotName) ? (zoneName + ' - ' + slotName) : '-';
    previewTime.textContent = formatNow();
  }

  function renderSlotGrid(typeId) {
    const allOptions = Array.from(slotSelect.options);
    const slotsForType = allOptions.filter(opt => opt.getAttribute('data-typeid') === typeId);

    const byZone = {};
    slotsForType.forEach(opt => {
      const zoneName = opt.getAttribute('data-zonename');
      if (!byZone[zoneName]) byZone[zoneName] = [];
      byZone[zoneName].push(opt);
    });

    slotGrid.innerHTML = '';

    Object.keys(byZone).forEach(zoneName => {
      const zoneBlock = document.createElement('div');
      zoneBlock.className = 'zone-block';

      const title = document.createElement('h4');
      title.textContent = zoneName;
      zoneBlock.appendChild(title);

      const slots = byZone[zoneName];
      let row;
      slots.forEach((opt, index) => {
        if (index % 5 === 0) {
          row = document.createElement('div');
          row.className = 'slot-row';
          zoneBlock.appendChild(row);
        }
        const status = (opt.getAttribute('data-status') || '').toUpperCase();
        const slotName = opt.getAttribute('data-slotname');
        const slotId = opt.value;

        const btn = document.createElement('button');
        btn.type = 'button';
        btn.textContent = slotName;
        btn.className = 'slot-cell';

        if (status === 'AVAILABLE') {
          btn.classList.add('slot-available');
        } else if (status === 'OCCUPIED') {
          btn.classList.add('slot-occupied');
        } else {
          btn.classList.add('slot-maintenance');
        }

        btn.addEventListener('click', function () {
          if (!btn.classList.contains('slot-available')) {
            return;
          }
          slotSelect.value = slotId;
          updatePreview();
        });

        row.appendChild(btn);
      });

      slotGrid.appendChild(zoneBlock);
    });
  }

  function filterSlotsByType() {
    const typeId = vehicleTypeSelect.value;

    let firstAvailableValue = null;
    for (let i = 0; i < slotSelect.options.length; i++) {
      const opt = slotSelect.options[i];
      const optType = opt.getAttribute('data-typeid');
      const status = (opt.getAttribute('data-status') || '').toUpperCase();
      const matchType = optType === typeId;
      const isAvailable = status === 'AVAILABLE';

      if (matchType && isAvailable) {
        opt.style.display = '';
        if (firstAvailableValue === null) {
          firstAvailableValue = opt.value;
        }
      } else {
        opt.style.display = 'none';
      }
    }

    if (firstAvailableValue !== null) {
      slotSelect.value = firstAvailableValue;
    } else {
      slotSelect.value = '';
    }

    renderSlotGrid(typeId);
    updatePreview();
  }

  vehicleTypeSelect.addEventListener('change', filterSlotsByType);
  slotSelect.addEventListener('change', updatePreview);
  if (licenseInput) {
    licenseInput.addEventListener('input', updatePreview);
  }

  filterSlotsByType();
})();

(function () {
  const vehicleTypeSelect = document.getElementById('vehicleType');
  const slotSelect = document.getElementById('assignedSlot');
  const slotGrid = document.getElementById('slotGrid');

  if (!vehicleTypeSelect || !slotSelect || !slotGrid) {
    return;
  }

  const previewPlate = document.getElementById('previewPlate');
  const previewType = document.getElementById('previewType');
  const previewSlot = document.getElementById('previewSlot');
  const previewTime = document.getElementById('previewTime');
  const licenseInput = document.getElementById('licensePlate');

  const typeNames = {
    '1': 'Motorbike',
    '2': 'Car',
    '3': 'Bicycle',
    '4': 'Electric Car'
  };

  function formatNow() {
    const now = new Date();
    return now.toLocaleString();
  }

  function updatePreview() {
    if (!previewPlate || !previewType || !previewSlot || !previewTime || !licenseInput) {
      return;
    }
    const plate = licenseInput.value || '-';
    const typeVal = vehicleTypeSelect.value;
    const typeText = typeNames[typeVal] || '-';
    const selectedOption = slotSelect.options[slotSelect.selectedIndex];
    const zoneName = selectedOption ? selectedOption.getAttribute('data-zonename') : null;
    const slotName = selectedOption ? selectedOption.getAttribute('data-slotname') : null;

    previewPlate.textContent = plate;
    previewType.textContent = typeText;
    previewSlot.textContent = (zoneName && slotName) ? (zoneName + ' - ' + slotName) : '-';
    previewTime.textContent = formatNow();
  }

  function renderSlotGrid(typeId) {
    const allOptions = Array.from(slotSelect.options);
    const slotsForType = allOptions.filter(opt => opt.getAttribute('data-typeid') === typeId);

    const byZone = {};
    slotsForType.forEach(opt => {
      const zoneName = opt.getAttribute('data-zonename');
      if (!byZone[zoneName]) byZone[zoneName] = [];
      byZone[zoneName].push(opt);
    });

    slotGrid.innerHTML = '';

    Object.keys(byZone).forEach(zoneName => {
      const zoneBlock = document.createElement('div');
      zoneBlock.className = 'zone-block';

      const title = document.createElement('h4');
      title.textContent = zoneName;
      zoneBlock.appendChild(title);

      const slots = byZone[zoneName];
      let row;
      slots.forEach((opt, index) => {
        if (index % 5 === 0) {
          row = document.createElement('div');
          row.className = 'slot-row';
          zoneBlock.appendChild(row);
        }
        const status = (opt.getAttribute('data-status') || '').toUpperCase();
        const slotName = opt.getAttribute('data-slotname');
        const slotId = opt.value;

        const btn = document.createElement('button');
        btn.type = 'button';
        btn.textContent = slotName;
        btn.className = 'slot-cell';

        if (status === 'AVAILABLE') {
          btn.classList.add('slot-available');
        } else if (status === 'OCCUPIED') {
          btn.classList.add('slot-occupied');
        } else {
          btn.classList.add('slot-maintenance');
        }

        btn.addEventListener('click', function () {
          if (!btn.classList.contains('slot-available')) {
            return;
          }
          slotSelect.value = slotId;
          updatePreview();
        });

        row.appendChild(btn);
      });

      slotGrid.appendChild(zoneBlock);
    });
  }

  function filterSlotsByType() {
    const typeId = vehicleTypeSelect.value;

    let firstAvailableValue = null;
    for (let i = 0; i < slotSelect.options.length; i++) {
      const opt = slotSelect.options[i];
      const optType = opt.getAttribute('data-typeid');
      const status = (opt.getAttribute('data-status') || '').toUpperCase();
      const matchType = optType === typeId;
      const isAvailable = status === 'AVAILABLE';

      if (matchType && isAvailable) {
        opt.style.display = '';
        if (firstAvailableValue === null) {
          firstAvailableValue = opt.value;
        }
      } else {
        opt.style.display = 'none';
      }
    }

    if (firstAvailableValue !== null) {
      slotSelect.value = firstAvailableValue;
    } else {
      slotSelect.value = '';
    }

    renderSlotGrid(typeId);
    updatePreview();
  }

  vehicleTypeSelect.addEventListener('change', filterSlotsByType);
  slotSelect.addEventListener('change', updatePreview);
  if (licenseInput) {
    licenseInput.addEventListener('input', updatePreview);
  }

  filterSlotsByType();
})();

