import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICarrera } from 'app/shared/model/carrera.model';
import { Modalidad } from 'app/shared/model/enumerations/modalidad.model';
import { getEntity, updateEntity, createEntity, reset } from './carrera.reducer';

export const CarreraUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const carreraEntity = useAppSelector(state => state.carrera.entity);
  const loading = useAppSelector(state => state.carrera.loading);
  const updating = useAppSelector(state => state.carrera.updating);
  const updateSuccess = useAppSelector(state => state.carrera.updateSuccess);
  const modalidadValues = Object.keys(Modalidad);

  const handleClose = () => {
    navigate('/carrera');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...carreraEntity,
      ...values,
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
          modalidad: 'PRESENCIAL',
          ...carreraEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="arqui3RealApp.carrera.home.createOrEditLabel" data-cy="CarreraCreateUpdateHeading">
            <Translate contentKey="arqui3RealApp.carrera.home.createOrEditLabel">Create or edit a Carrera</Translate>
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
                  id="carrera-id"
                  label={translate('arqui3RealApp.carrera.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('arqui3RealApp.carrera.nombre')}
                id="carrera-nombre"
                name="nombre"
                data-cy="nombre"
                type="text"
              />
              <ValidatedField
                label={translate('arqui3RealApp.carrera.modalidad')}
                id="carrera-modalidad"
                name="modalidad"
                data-cy="modalidad"
                type="select"
              >
                {modalidadValues.map(modalidad => (
                  <option value={modalidad} key={modalidad}>
                    {translate('arqui3RealApp.Modalidad.' + modalidad)}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/carrera" replace color="info">
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

export default CarreraUpdate;
