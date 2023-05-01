import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IIngresoEstudiante } from 'app/shared/model/ingreso-estudiante.model';
import { getEntities as getIngresoEstudiantes } from 'app/entities/ingreso-estudiante/ingreso-estudiante.reducer';
import { IDocumentoIngresoEstudiante } from 'app/shared/model/documento-ingreso-estudiante.model';
import { getEntity, updateEntity, createEntity, reset } from './documento-ingreso-estudiante.reducer';

export const DocumentoIngresoEstudianteUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const ingresoEstudiantes = useAppSelector(state => state.ingresoEstudiante.entities);
  const documentoIngresoEstudianteEntity = useAppSelector(state => state.documentoIngresoEstudiante.entity);
  const loading = useAppSelector(state => state.documentoIngresoEstudiante.loading);
  const updating = useAppSelector(state => state.documentoIngresoEstudiante.updating);
  const updateSuccess = useAppSelector(state => state.documentoIngresoEstudiante.updateSuccess);

  const handleClose = () => {
    navigate('/documento-ingreso-estudiante');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getIngresoEstudiantes({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...documentoIngresoEstudianteEntity,
      ...values,
      ingresoEstudiante: ingresoEstudiantes.find(it => it.id.toString() === values.ingresoEstudiante.toString()),
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
          ...documentoIngresoEstudianteEntity,
          ingresoEstudiante: documentoIngresoEstudianteEntity?.ingresoEstudiante?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="arqui3RealApp.documentoIngresoEstudiante.home.createOrEditLabel" data-cy="DocumentoIngresoEstudianteCreateUpdateHeading">
            <Translate contentKey="arqui3RealApp.documentoIngresoEstudiante.home.createOrEditLabel">
              Create or edit a DocumentoIngresoEstudiante
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
                  id="documento-ingreso-estudiante-id"
                  label={translate('arqui3RealApp.documentoIngresoEstudiante.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('arqui3RealApp.documentoIngresoEstudiante.nombreDocumento')}
                id="documento-ingreso-estudiante-nombreDocumento"
                name="nombreDocumento"
                data-cy="nombreDocumento"
                type="text"
              />
              <ValidatedField
                label={translate('arqui3RealApp.documentoIngresoEstudiante.descripcionDocumento')}
                id="documento-ingreso-estudiante-descripcionDocumento"
                name="descripcionDocumento"
                data-cy="descripcionDocumento"
                type="text"
              />
              <ValidatedField
                id="documento-ingreso-estudiante-ingresoEstudiante"
                name="ingresoEstudiante"
                data-cy="ingresoEstudiante"
                label={translate('arqui3RealApp.documentoIngresoEstudiante.ingresoEstudiante')}
                type="select"
              >
                <option value="" key="0" />
                {ingresoEstudiantes
                  ? ingresoEstudiantes.map(otherEntity => (
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
                to="/documento-ingreso-estudiante"
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

export default DocumentoIngresoEstudianteUpdate;
