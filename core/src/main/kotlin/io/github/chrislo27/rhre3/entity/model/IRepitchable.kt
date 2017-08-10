package io.github.chrislo27.rhre3.entity.model

import io.github.chrislo27.rhre3.registry.GameRegistry
import io.github.chrislo27.rhre3.registry.datamodel.ContainerModel
import io.github.chrislo27.rhre3.registry.datamodel.impl.Cue


interface IRepitchable {

    companion object {
//        fun anyInList(list: List<Entity>): Lazy<Boolean> {
//            return lazy {
//                list.any {
//                    (it as? IRepitchable)?.canBeRepitched == true
//                }
//            }
//        }

        fun anyInModel(model: ContainerModel): Lazy<Boolean> {
            return lazy {
                model.cues.any {
                    (GameRegistry.data.objectMap[it.id] as? Cue)?.repitchable == true
                }
            }
        }
    }

    var semitone: Int
    val canBeRepitched: Boolean

}