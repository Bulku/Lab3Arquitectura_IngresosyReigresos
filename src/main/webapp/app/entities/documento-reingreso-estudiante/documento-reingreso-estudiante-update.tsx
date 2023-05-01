import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISolicitudReingreso } from 'app/shared/model/solicitud-reingreso.model';
import { getEntities as getSolicitudReingresos } from 'app/entities/solicitud-reingreso/solicitud-reingreso.reducer';
import { IDocumentoReingresoEstudiante } from 'app/shared/model/documento-reingreso-estudiante.model';
import { getEntity, updateEntity, createEntity, reset } from './documento-reingreso-estudiante.reducer';

export const DocumentoReingresoEstudianteUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const solicitudReingresos = useAppSelector(state => state.solicitudReingreso.entities);
  const documentoReingresoEstudianteEntity = useAppSelector(state => state.documentoReingresoEstudiante.entity);
  const loading = useAppSelector(state => state.documentoReingresoEstudiante.loading);
  const updating = useAppSelector(state => state.documentoReingresoEstudiante.updating);
  const updateSuccess = useAppSelector(state => state.documentoReingresoEstudiante.updateSuccess);

  const handleClose = () => {
    navigate('/documento-reingreso-estudiante');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getSolicitudReingresos({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...documentoReingresoEstudianteEntity,
      ...values,
      solicitudReingreso: solicitudReingresos.find(it => it.id.toString() === values.solicitudReingreso.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...documentoReingresoEstudianteEntity,
          solicitudReingreso: documentoReingresoEstudianteEntity?.solicitudReingreso?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2
            id="arqui3RealApp.documentoReingresoEstudiante.home.createOrEditLabel"
            data-cy="DocumentoReingresoEstudianteCreateUpdateHeading"
          >
            <Translate contentKey="arqui3RealApp.documentoReingresoEstudiante.home.createOrEditLabel">
              Create or edit a DocumentoReingresoEstudiante
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="documento-reingreso-estudiante-id"
                  label={translate('arqui3RealApp.documentoReingresoEstudiante.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('arqui3RealApp.documentoReingresoEstudiante.nombreDocumento')}
                id="documento-reingreso-estudiante-nombreDocumento"
                name="nombreDocumento"
                data-cy="nombreDocumento"
                type="text"
              />
              <ValidatedField
                label={translate('arqui3RealApp.documentoReingresoEstudiante.descripcionDocumento')}
                id="documento-reingreso-estudiante-descripcionDocumento"
                name="descripcionDocumento"
                data-cy="descripcionDocumento"
                type="text"
              />
              <ValidatedField
                id="documento-reingreso-estudiante-solicitudReingreso"
                name="solicitudReingreso"
                data-cy="solicitudReingreso"
                label={translate('arqui3RealApp.documentoReingresoEstudiante.solicitudReingreso')}
                type="select"
              >
                <option value="" key="0" />
                {solicitudReingresos
                  ? solicitudReingresos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button
                tag={Link}
                id="cancel-save"
                data-cy="entityCreateCancelButton"
                to="/documento-reingreso-estudiante"
                replace
                color="info"
              >
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default DocumentoReingresoEstudianteUpdate;
