import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/estudiante">
        <Translate contentKey="global.menu.entities.estudiante" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/solicitud-reingreso">
        <Translate contentKey="global.menu.entities.solicitudReingreso" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/documento-reingreso-estudiante">
        <Translate contentKey="global.menu.entities.documentoReingresoEstudiante" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/carrera">
        <Translate contentKey="global.menu.entities.carrera" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/pensum">
        <Translate contentKey="global.menu.entities.pensum" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/materia">
        <Translate contentKey="global.menu.entities.materia" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/sede">
        <Translate contentKey="global.menu.entities.sede" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/ingreso-estudiante">
        <Translate contentKey="global.menu.entities.ingresoEstudiante" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/documento-ingreso-estudiante">
        <Translate contentKey="global.menu.entities.documentoIngresoEstudiante" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
